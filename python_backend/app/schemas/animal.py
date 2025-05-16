from datetime import date
from pydantic import BaseModel
from typing import List, Optional, Dict

class ValueSchema(BaseModel):
    value: str

class AttributeSchema(BaseModel):
    name: str
    value: Optional[str] = None

class ParameterStatsSchema(BaseModel):
    parameter: str
    min: Optional[float] = None
    max: Optional[float] = None
    avg: Optional[float] = None
    last: Optional[float] = None

class AnimalResponse(BaseModel):
    id: int
    name: str
    description: Optional[str] = None
    birth_date: Optional[date] = None
    mass: Optional[float] = None
    height: Optional[float] = None
    temperature: Optional[float] = None
    activity_level: Optional[int] = None
    appetite_level: Optional[int] = None
    attributes: List[AttributeSchema]
    parameter_stats: List[ParameterStatsSchema]