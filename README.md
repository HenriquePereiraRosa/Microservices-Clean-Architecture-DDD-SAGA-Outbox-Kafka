# Microservices: Clean Architecture, DDD, SAGA, Outbox & Kafka

## GraphViz to Visualize dependencies:
[GraphViz](https://github.com/ferstl/depgraph-maven-plugin)
```
mvn com.github.ferstl:depgraph-maven-plugin:aggregate -DcreateImage=true -DreduceEdges=false -Dscope=compile "-Dincludes=com.h.udemy*:*"
```

## DATABASE
### create db:
```
docker-compose -f  ./infrastructure/docker-files/db.yml up
```
### db tables:
````
select * from information_schema.tables;

select * from customer.vw_order_customer;

select * from restaurant.products;
select * from restaurant.restaurant_products;
select * from restaurant.restaurants;
select * from restaurant.order_approval;
select * from restaurant.order_restaurant_m_view;

select * from "order".orders;
select * from "order".order_items;
select * from "order".order_address;
select * from "order".payment_outbox;
select * from "order".customers;

select * from customer.customers;

select * from payment.payments;
select * from payment.credit_entry;
select * from payment.credit_history;


[//]: # (DESCRIBE A COLUMN IN POSTGRES)
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns
WHERE table_schema = 'payment'
  AND table_name = 'order_outbox';
````


## KAFKA:
## remove files:
```
./kafka-init.sh
```

## run kafka:
```
./kafka-run.sh
```

## REQUESTS

### create order:
```
POST http://localhost:8181/v1/orders/
```
```
{
    "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41",
    "restaurantId": "d215b5f8-0249-4dc5-89a3-51fd148cfb45",
    "address": {
        "street":  "street_01",
        "postalCode": "1000AB",
        "city": "Amsterdam"
    },
    "price": 200.00,
    "items": [
        {
            "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
            "quantity": 1,
            "price": 50.00
        },
        {
            "productId": "d215b5f8-0249-4dc5-89a3-51fd148cfb48",
            "quantity": 3,
            "price": 50.00
        }
    ]
}
```

### check order status:
```
GET http://localhost:8181/v1/orders/by-tracking-id?trackingId=f00d195f-07ca-4d41-8e7d-5743dae09e67
```







## Add your files

- [ ] [Create](https://gitlab.com/-/experiment/new_project_readme_content:1381cfbd39c01a125285e191fa128fc2?https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://gitlab.com/-/experiment/new_project_readme_content:1381cfbd39c01a125285e191fa128fc2?https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://gitlab.com/-/experiment/new_project_readme_content:1381cfbd39c01a125285e191fa128fc2?https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

