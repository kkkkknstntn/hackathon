from fastapi import APIRouter, Depends, Header, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import get_deepseek_service
from app.services import AnimalService, DeepSeekService
from app.schemas.deepseek import DeepSeekAnalysisRequest, DeepSeekAnalysisResponse
from app.core.database import get_db

router = APIRouter()

@router.post("/analyze", response_model=DeepSeekAnalysisResponse)
async def analyze_animal_data(
        request: DeepSeekAnalysisRequest,
        x_user_name: str = Header(None, convert_underscores=True),
        db: AsyncSession = Depends(get_db),
        deepseek_service: DeepSeekService = Depends(get_deepseek_service)
):
    try:
        username = x_user_name #or "chikernut213@gmail.com"

        async with db.begin():
            animal_data = await AnimalService.get_animal_by_user(db, username, request.animal_id)
            result = await deepseek_service.analyze_animal_data(animal_data)
            await db.commit()
            return result

    except ValueError as e:
        raise HTTPException(status_code=404, detail=str(e))
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Analysis failed: {str(e)}"
        )