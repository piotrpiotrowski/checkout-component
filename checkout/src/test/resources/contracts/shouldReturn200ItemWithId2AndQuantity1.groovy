import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/customers/1/orders"
        body("""
          {
          "items": [
            {
              "id": "2",
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
   "summaryPrice": 10,
  "items": [
    {
      "id": "2",
      "quantity": 1,
      "price": 10
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}