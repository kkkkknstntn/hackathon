from sqlalchemy import Column, String, Boolean
from sqlalchemy.orm import relationship, Mapped, mapped_column
from .base import Base

class User(Base):
    __tablename__ = "users"
    id: Mapped[int] = mapped_column(primary_key=True)
    email = Column(String(255), unique=True, index=True)
    username = Column(String(255), unique=True, index=True)
    first_name = Column(String(255))
    last_name = Column(String(255))
    city = Column(String(255))
    about_me = Column(String(255))
    is_active = Column(Boolean, default=True)

    # Relationships
    animals = relationship("AnimalUser", back_populates="user")
    parameter_histories = relationship("AnimalParameterHistory", back_populates="user")
    status_logs = relationship("AnimalStatusLog", back_populates="user")