from pydantic import BaseModel

class BERTPredictionRequest(BaseModel):
    animal_class: str
    description: str

class BERTPredictionResponse(BaseModel):
    is_dangerous: bool