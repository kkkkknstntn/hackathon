from typing import AsyncGenerator

from sqlalchemy.ext.asyncio import AsyncSession

from app.core.database import AsyncSessionFactory
from app.services import (
    BERTService,
    DeepSeekService,
    AnimalService,
)

def get_bert_service() -> BERTService:
    return BERTService().get_instance()

def get_deepseek_service() -> DeepSeekService:
    return DeepSeekService()

def get_animal_service() -> AnimalService:
    return AnimalService()