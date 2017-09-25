import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/customers/1/orders"
        body("""
        {
        "items": [{
                      "id"      : 1,
                      "quantity": 3
              },
              {
                      "id"      : 2,
                      "quantity": 2
              },
              {
                      "id"      : 3,
                      "quantity": 4
            }]
        } """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
    response {
        status 201
        body("""
  {
  "summaryPrice": 185,
  "items": [
    {
      "id": "1",
      "quantity": 3,
      "price": 90
    },
    {
      "id": "2",
      "quantity": 2,
      "price": 15
    },
    {
      "id": "3",
      "quantity": 4,
      "price": 80
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}