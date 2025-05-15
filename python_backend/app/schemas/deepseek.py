from pydantic import BaseModel
from typing import List, Optional
from app.schemas.animal import AnimalResponse, ParameterStatsSchema

class DeepSeekAnalysisRequest(BaseModel):
    animal_id: int

class DeepSeekMessage(BaseModel):
    role: str
    content: str

class DeepSeekAnalysisResponse(BaseModel):
    advice: str
