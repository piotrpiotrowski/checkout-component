package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/customers/1/orders"
        body("""
      {
      "items": [
        {
          "id": "1",
          "quantity": 3
        },
        {
          "id": "4",
          "quantity": 1
        }
      ]
    }
    """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
    response {
        status 201
        body("""
  {
  "summaryPrice": 102.5,
  "items": [
    {
      "id": "1",
      "quantity": 3,
      "price": 90
    },
    {
      "id": "4",
      "quantity": 1,
      "price": 12.5
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}