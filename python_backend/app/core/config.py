from pydantic_settings import BaseSettings, SettingsConfigDict
from pydantic import PostgresDsn

class Settings(BaseSettings):
    PROJECT_NAME: str
    API_V1_STR: str
    ###
    POSTGRES_SERVER: str
    POSTGRES_USER: str
    POSTGRES_PASSWORD: str
    POSTGRES_DB: str

    ###
    EUREKA_HOST: str
    EUREKA_PORT: int
    EUREKA_NAME: str
    ###
    APP_NAME: str
    INSTANCE_PORT: int
    SERVER_PORT: int
    APP_HOST: str
    ###
    DEEPSEEK_API_KEY: str
    DEEPSEEK_API_URL: str
    DEEPSEEK_MODEL: str
    ###
    BERT_MODEL_PATH: str
    ###
    @property
    def DATABASE_URL(self) -> str:
        return f"postgresql+asyncpg://{self.POSTGRES_USER}:{self.POSTGRES_PASSWORD}@{self.POSTGRES_SERVER}/{self.POSTGRES_DB}"

    # @property
    # def DATABASE_URL(self) -> str:
    #     return f"postgresql+asyncpg://{self.POSTGRES_USER}:{self.POSTGRES_PASSWORD}@{self.POSTGRES_SERVER}:{self.POSTGRES_PORT}/{self.POSTGRES_DB}"

    @property
    def EUREKA_SERVER_config(self):
        eureka_url = f"http://{self.EUREKA_HOST}:{self.EUREKA_PORT}/{self.EUREKA_NAME}"
        return {
            "app_name": self.APP_NAME,
            "eureka_server": eureka_url,
            "instance_host": self.APP_HOST,
            "instance_port": self.SERVER_PORT,
            "eureka_protocol": "http",
            "should_register": True,
            "should_discover": True,
        }
    model_config = SettingsConfigDict(env_file=".env")


settings = Settings()