db.orders.insertMany([
    {
        userId: "1",
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
                productId: "product1",
                productPrice: 19.99,
                total: 39.98
            },
            {
                quantity: 1,
                productId: "product2",
                productPrice: 24.99,
                total: 24.99
            }
        ]
    },
    {
        userId: "2",
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
                productId: "product3",
                productPrice: 29.99,
                total: 89.97
            }
        ]
    }
]);
