from sqlalchemy import Column, String, ForeignKey, SmallInteger
from sqlalchemy.orm import relationship, Mapped, mapped_column
from .base import Base


class Attribute(Base):
    __tablename__ = "attribute"
    id: Mapped[int] = mapped_column(primary_key=True)

    name = Column(String(255))
    animal_id = Column(ForeignKey("animal.id"))

    # Relationships
    animal = relationship("Animal", back_populates="attributes")
    values = relationship("Value", back_populates="attribute", cascade="all, delete-orphan", uselist=False)


class Value(Base):
    __tablename__ = "value"
    id: Mapped[int] = mapped_column(primary_key=True)
    value = Column(String(1024))
    attribute_id = Column(ForeignKey("attribute.id"))

    # Relationships
    attribute = relationship("Attribute", back_populates="values")