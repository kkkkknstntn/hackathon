import httpx
import logging
from fastapi import HTTPException
import httpx
import logging
from app.core.config import settings
from app.schemas import AnimalResponse
from app.schemas.deepseek import DeepSeekAnalysisRequest, DeepSeekMessage, DeepSeekAnalysisResponse
from typing import List, Optional
import json
from tenacity import retry, stop_after_attempt, wait_exponential, retry_if_exception_type

logger = logging.getLogger(__name__)


class DeepSeekService:
    def __init__(self):
        self.api_url = f"{settings.DEEPSEEK_API_URL}/chat/completions"
        self.headers = {
            "Authorization": f"Bearer {settings.DEEPSEEK_API_KEY}",
            "Content-Type": "application/json"
        }
        self.timeout = httpx.Timeout(60.0, connect=10.0)  # Увеличиваем таймауты

    @retry(
        stop=stop_after_attempt(3),
        wait=wait_exponential(multiplier=1, min=4, max=10),
        retry=retry_if_exception_type((httpx.ReadTimeout, httpx.NetworkError)),
        reraise=True
    )
    async def analyze_animal_data(
            self,
            animal_data: AnimalResponse,
            user_question: Optional[str] = None
    ) -> DeepSeekAnalysisResponse:
        try:

            messages = self._prepare_messages(animal_data, user_question)
            payload = {
                "model": settings.DEEPSEEK_MODEL,
                "messages": [msg.dict() for msg in messages],
                "temperature": 0.7
            }

            logger.debug(
                "Sending request to DeepSeek API:\nURL: %s\nPayload: %s",
                self.api_url,
                json.dumps(payload, indent=2, ensure_ascii=False)
            )

            async with httpx.AsyncClient(timeout=self.timeout) as client:
                try:
                    response = await client.post(
                        self.api_url,
                        json=payload,
                        headers=self.headers
                    )

                    logger.debug(
                        "Raw DeepSeek API response:\nStatus: %d\nBody: %s",
                        response.status_code,
                        response.text
                    )

                    response.raise_for_status()
                    api_response = response.json()

                    result = self._parse_response(api_response)

                    return result

                except httpx.ReadTimeout as e:
                    logger.error(
                        "DeepSeek API timeout. URL: %s, Timeout: %s",
                        self.api_url,
                        str(self.timeout)
                    )
                    raise
                except httpx.HTTPStatusError as e:
                    logger.error(
                        "DeepSeek API error. Status: %d, Response: %s",
                        e.response.status_code,
                        e.response.text
                    )
                    raise
                except httpx.NetworkError as e:
                    logger.error("Network error during DeepSeek API call: %s", str(e))
                    raise
                except Exception as e:
                    logger.exception("Unexpected error during DeepSeek API call")
                    raise

        except Exception as e:
            logger.exception(
                "Failed to analyze animal %s after retries",
                animal_data.name
            )
            raise HTTPException(
                status_code=500,
                detail=f"Analysis failed: {str(e)}"
            )


    def _prepare_messages(
            self,
            animal_data: AnimalResponse,
            user_question: Optional[str]
    ) -> List[DeepSeekMessage]:
        base_prompt = self._create_base_prompt(animal_data)
        messages = [
            DeepSeekMessage(
                role="system",
                content="Ты - ветеринарный помощник. Анализируй данные о животных и давай рекомендации."
            ),
            DeepSeekMessage(role="user", content=base_prompt)
        ]

        if user_question:
            messages.append(
                DeepSeekMessage(role="user", content=user_question)
            )

        logger.debug("Prepared messages for DeepSeek:\n%s", messages)
        return messages

    def _create_base_prompt(self, animal_data: AnimalResponse) -> str:
        attributes = "\n".join(
            f"- {attr.name}: {attr.value if attr.value else 'не указано'}"
            for attr in animal_data.attributes
        )

        stats = "\n".join(
            f"- {stat.parameter}: min={stat.min}, max={stat.max}, avg={stat.avg}, last={stat.last}"
            for stat in animal_data.parameter_stats
        )

        prompt = f"""
        Данные животного:
        Имя: {animal_data.name}
        Описание: {animal_data.description or 'не указано'}
        Возраст: {animal_data.birth_date or 'не указано'}

        Атрибуты:
        {attributes or 'нет атрибутов'}

        Статистика параметров:
        {stats or 'нет данных'}

        Проанализируй состояние животного и дай рекомендации.
        """

        logger.debug("Generated prompt:\n%s", prompt)
        return prompt

    def _parse_response(self, api_response: dict) -> DeepSeekAnalysisResponse:
        content = api_response["choices"][0]["message"]["content"]

        logger.debug("Raw content from DeepSeek:\n%s", content)

        return DeepSeekAnalysisResponse(
            advice=content
        )

    def _format_debug_data(self, animal_data: AnimalResponse) -> str:
        data = {
            "id": animal_data.id,
            "name": animal_data.name,
            "description": animal_data.description,
            "birth_date": str(animal_data.birth_date),
            "attributes": [
                {"name": attr.name, "value": attr.value}
                for attr in animal_data.attributes
            ],
            "parameter_stats": [
                {
                    "parameter": stat.parameter,
                    "min": stat.min,
                    "max": stat.max,
                    "avg": stat.avg,
                    "last": stat.last
                }
                for stat in animal_data.parameter_stats
            ]
        }
        return json.dumps(data, indent=2, ensure_ascii=False)