Java + JerseyRESTService + PG
приложение «Сервис статистики»

1.	Запустить docker-compose.yml – для создания БД и структуры данных 
2.	Запустить приложение StatisticServices (порт 8080) 
3.	Запустить либо StatisticClient, либо в postman 
4.	БД настроена на порт 5433 PG 
Пример 
http://localhost:8080/StatisticServices/api/person/add 
{ 
    "sex": 0, 
    "dtOfBirth": "01.01.1982", 
    "region": 56, 
    "income": null 
} 
