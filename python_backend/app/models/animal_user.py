from sqlalchemy import Column, ForeignKey
from sqlalchemy.orm import relationship, Mapped, mapped_column
from .base import Base


class AnimalUser(Base):
    __tablename__ = "animal_user"

    id: Mapped[int] = mapped_column(primary_key=True)

    user_id = Column(ForeignKey("users.id"))
    animal_id = Column(ForeignKey("animal.id"))

    # Relationships
    user = relationship("User", back_populates="animals")
    animal = relationship("Animal", back_populates="users")