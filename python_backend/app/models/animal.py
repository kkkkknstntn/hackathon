from sqlalchemy import Column, String, Numeric, Integer, Date, Text, DateTime
from sqlalchemy.orm import relationship, Mapped, mapped_column
from .base import Base
from sqlalchemy.sql import func

class Animal(Base):
    __tablename__ = "animal"
    id: Mapped[int] = mapped_column(primary_key=True)
    name = Column(String(255))
    description = Column(Text)
    birth_date = Column(Date)
    body_mass = Column(Numeric(10, 2))
    height = Column(Numeric(10, 2))
    temperature = Column(Numeric(5, 2))
    activity_level = Column(Integer)
    appetite_level = Column(Integer)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

    # Relationships
    users = relationship("AnimalUser", back_populates="animal")
    attributes = relationship("Attribute", back_populates="animal", cascade="all, delete-orphan")
    parameter_histories = relationship("AnimalParameterHistory", back_populates="animal")
    status_logs = relationship("AnimalStatusLog", back_populates="animal")