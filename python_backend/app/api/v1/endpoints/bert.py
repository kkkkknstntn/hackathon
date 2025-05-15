from fastapi import APIRouter, Depends, HTTPException

from app.core.dependencies import get_bert_service
from app.services.bert_service import BERTService
from app.schemas.bert import BERTPredictionRequest, BERTPredictionResponse

router = APIRouter()

@router.post("/predict", response_model=BERTPredictionResponse)
async def predict_danger(
    request: BERTPredictionRequest,
    bert_service: BERTService = Depends(get_bert_service)
):
    try:
        is_dangerous = await bert_service.predict_danger(
            request.animal_class,
            request.description
        )
        return {"is_dangerous": is_dangerous}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))