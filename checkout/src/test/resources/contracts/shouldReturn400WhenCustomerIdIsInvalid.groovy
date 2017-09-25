import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/customers/-1/orders"
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
        status 400
        body("""
        {
        "message": "Validation Failed",
        "code": "BAD_REQUEST",
        "details":[
        {
         "code": "negative-value",
         "field":"customerId",
         "value":-1
        }
    ]
       }
        """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}