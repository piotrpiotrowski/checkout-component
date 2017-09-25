import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/customers/1/orders"
        body("""
        {
        "items": [{
                      "id"      : 1,
                      "quantity": 2
              },
              {
                      "id"      : 2,
                      "quantity": 1
              },
              {
                      "id"      : 3,
                      "quantity": 3
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
  "summaryPrice": 180,
  "items": [
    {
      "id": "1",
      "quantity": 2,
      "price": 80
    },
    {
      "id": "2",
      "quantity": 1,
      "price": 10
    },
    {
      "id": "3",
      "quantity": 3,
      "price": 90
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}