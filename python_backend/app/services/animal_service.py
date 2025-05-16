from sqlalchemy import select, func
from sqlalchemy.ext.asyncio import AsyncSession
from app.models import Animal, User, AnimalUser, Attribute, AnimalParameterHistory, Value
from app.schemas.animal import AnimalResponse, ParameterStatsSchema, AttributeSchema
from typing import List, Dict


class AnimalService:
    @staticmethod
    async def get_animal_by_user(
            db: AsyncSession,
            username: str,
            animal_id: int
    ) -> AnimalResponse:
        try:
            # Получаем животное
            stmt = (
                select(Animal)
                .join(AnimalUser, AnimalUser.animal_id == Animal.id)
                .join(User, User.id == AnimalUser.user_id)
                .where(User.username == username)
                .where(Animal.id == animal_id)
            )
            animal = (await db.execute(stmt)).scalar_one_or_none()

            if not animal:
                raise ValueError("Animal not found or access denied")

            # Получаем атрибуты с JOIN на values
            stmt_attrs = (
                select(Attribute, Value)
                .outerjoin(Value, Value.attribute_id == Attribute.id)
                .where(Attribute.animal_id == animal_id)
            )

            result = await db.execute(stmt_attrs)
            attributes = []
            for attr, value in result:
                attributes.append(AttributeSchema(
                    name=attr.name,
                    value=value.value if value else None
                ))

            parameter_stats = await AnimalService._get_parameter_stats(db, animal_id)

            return AnimalResponse(
                id=animal.id,
                name=animal.name,
                description=animal.description,
                birth_date=animal.birth_date,
                mass=float(animal.body_mass) if animal.body_mass else None,
                height=float(animal.height) if animal.height else None,
                temperature=float(animal.temperature) if animal.temperature else None,
                activity_level=animal.activity_level,
                appetite_level=animal.appetite_level,
                attributes=attributes,
                parameter_stats=parameter_stats
            )
        except Exception as e:
            await db.rollback()
            raise

    @staticmethod
    async def _get_parameter_stats(
            db: AsyncSession,
            animal_id: int
    ) -> List[ParameterStatsSchema]:
        # Common parameters to analyze
        parameters = {
            "mass": AnimalParameterHistory.new_mass,
            "height": AnimalParameterHistory.new_height,
            "temperature": AnimalParameterHistory.new_temperature,
            "activity_level": AnimalParameterHistory.new_activity_level,
            "appetite_level": AnimalParameterHistory.new_appetite_level
        }

        stats = []

        for param_name, param_field in parameters.items():
            # Get last value
            stmt_last = (
                select(param_field)
                .where(AnimalParameterHistory.animal_id == animal_id)
                .order_by(AnimalParameterHistory.recorded_at.desc())
                .limit(1)
            )
            last_value = (await db.execute(stmt_last)).scalar_one_or_none()

            stmt_agg = (
                select(
                    func.min(param_field).label("min"),
                    func.max(param_field).label("max"),
                    func.avg(param_field).label("avg")
                )
                .where(AnimalParameterHistory.animal_id == animal_id)
                .where(param_field.isnot(None))
            )
            agg_result = (await db.execute(stmt_agg)).first()

            stats.append(ParameterStatsSchema(
                parameter=param_name,
                min=agg_result.min if agg_result else None,
                max=agg_result.max if agg_result else None,
                avg=agg_result.avg if agg_result else None,
                last=last_value
            ))

        return stats