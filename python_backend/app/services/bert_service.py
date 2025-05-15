import os
import torch
import logging
from typing import Tuple, Optional
from fastapi import HTTPException
from transformers import BertForSequenceClassification, BertTokenizer

logger = logging.getLogger(__name__)


class BERTDangerClassifier:
    _instance = None

    def __init__(self, model_path: str, max_len: int = 128):
        try:
            self.model = BertForSequenceClassification.from_pretrained(
                model_path,
                local_files_only=True
            )
            self.tokenizer = BertTokenizer.from_pretrained(
                model_path,
                local_files_only=True
            )
            self.max_len = max_len
            self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
            self.model.to(self.device)
            self.model.eval()
            logger.info(f"Model loaded from {model_path} on {self.device}")
        except Exception as e:
            logger.error(f"Model loading failed: {str(e)}")
            raise HTTPException(
                status_code=500,
                detail=f"Model initialization error: {str(e)}"
            )

    @classmethod
    def get_instance(cls, model_path: Optional[str] = None):
        if cls._instance is None:
            if model_path is None:
                model_path = os.getenv("BERT_MODEL_PATH", "symptom_classifier")
            cls._instance = cls(model_path)
        return cls._instance

    def predict(self, animal_class: str, description: str, threshold: float = 0.995) -> Tuple[float, bool]:
        try:
            if not animal_class or not description:
                raise ValueError("Animal class and description must be provided")

            text = f"Животное: {animal_class}. Симптомы: {description}"
            encoding = self.tokenizer.encode_plus(
                text,
                max_length=self.max_len,
                add_special_tokens=True,
                return_token_type_ids=False,
                padding='max_length',
                truncation=True,
                return_attention_mask=True,
                return_tensors='pt',
            )

            input_ids = encoding['input_ids'].to(self.device)
            attention_mask = encoding['attention_mask'].to(self.device)

            with torch.no_grad():
                outputs = self.model(input_ids=input_ids, attention_mask=attention_mask)
                probs = torch.nn.functional.softmax(outputs.logits, dim=1)
                danger_prob = probs[0][1].item()

            return danger_prob, danger_prob >= threshold

        except Exception as e:
            logger.error(f"Prediction error: {str(e)}")
            raise HTTPException(status_code=500, detail=str(e))


class BERTService:
    _instance = None

    def __init__(self):
        self.classifier = BERTDangerClassifier.get_instance()

    @classmethod
    def get_instance(cls):
        if cls._instance is None:
            cls._instance = cls()
        return cls._instance

    async def predict_danger(self, animal_class: str, description: str) -> bool:
        _, is_dangerous = self.classifier.predict(animal_class, description)
        return is_dangerous