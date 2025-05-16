from sqlalchemy import Column, Numeric, Integer, Date, ForeignKey

from .base import Base
from sqlalchemy.orm import relationship, Mapped, mapped_column

class AnimalParameterHistory(Base):
    __tablename__ = "animal_parameter_history"
    id: Mapped[int] = mapped_column(primary_key=True)
    recorded_at = Column(Date)

    old_mass = Column(Numeric(10, 2))
    old_height = Column(Numeric(10, 2))
    old_temperature = Column(Numeric(5, 2))
    old_activity_level = Column(Integer)
    old_appetite_level = Column(Integer)

    new_mass = Column(Numeric(10, 2))
    new_height = Column(Numeric(10, 2))
    new_temperature = Column(Numeric(5, 2))
    new_activity_level = Column(Integer)
    new_appetite_level = Column(Integer)

    animal_id = Column(ForeignKey("animal.id"))
    user_id = Column(ForeignKey("users.id"))
    status_log_id = Column(ForeignKey("animal_status_log.id"))

    animal = relationship("Animal", back_populates="parameter_histories")
    user = relationship("User", back_populates="parameter_histories")
    status_log = relationship("AnimalStatusLog", back_populates="parameter_histories")