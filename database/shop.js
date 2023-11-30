db.orders.insertMany([
    {
        userId: "a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b",
        client: {
            fullName: "John Doe",
            email: "john.doe@example.com",
            phone: "123456789",
            address: {
                street: "Main Street",
                number: "123",
                city: "Cityville",
                province: "Provinceland",
                country: "Countryland",
                cp: "12345"
            }
        },
        orderLineList: [
            {
                quantity: 2,
                productId: "57b77805-c1ce-490f-a96f-ec15505d5fae",
                productPrice: 19.99,
                total: 39.98
            },
            {
                quantity: 1,
                productId: "874f872f-c30e-4089-ab33-16fd1c4d4344",
                productPrice: 24.99,
                total: 24.99
            }
        ]
    },
    {
        userId: "a2bcb7b3-4c36-4073-9ab8-9d8f4a57301b",
        client: {
            fullName: "Jane Doe",
            email: "jane.doe@example.com",
            phone: "987654321",
            address: {
                street: "Broadway",
                number: "456",
                city: "Citytown",
                province: "Stateland",
                country: "Countryland",
                cp: "54321"
            }
        },
        orderLineList: [
            {
                quantity: 3,
                productId: "57b77805-c1ce-490f-a96f-ec15505d5fae",
                productPrice: 29.99,
                total: 89.97
            }
        ]
    }
]);
