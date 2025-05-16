from sqlalchemy import Column, Text, Numeric, Integer, Date, ForeignKey
from .base import Base
from sqlalchemy.orm import relationship, Mapped, mapped_column

class AnimalStatusLog(Base):
    __tablename__ = "animal_status_log"
    id: Mapped[int] = mapped_column(primary_key=True)
    log_date = Column(Date)
    notes = Column(Text)

    mass_change = Column(Numeric(10, 2))
    height_change = Column(Numeric(10, 2))
    temperature_change = Column(Numeric(5, 2))
    activity_level_change = Column(Integer)
    appetite_level_change = Column(Integer)

    animal_id = Column(ForeignKey("animal.id"))
    user_id = Column(ForeignKey("users.id"))

    animal = relationship("Animal", back_populates="status_logs")
    user = relationship("User", back_populates="status_logs")
    parameter_histories = relationship("AnimalParameterHistory", back_populates="status_log")