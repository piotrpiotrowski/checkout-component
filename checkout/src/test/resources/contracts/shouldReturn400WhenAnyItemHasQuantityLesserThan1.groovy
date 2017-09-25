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
              "quantity": 1
            },
            {
              "id": "2",
              "quantity": 0
            },
            {
              "id": "3",
              "quantity": -1
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
        "message": "Item quantity has to be greater than 0",
        "code": "invalid-items-list",
        "details":[
        {
         "code": "negative-number",
         "field":"invalidItemsIds",
         "value":[2,3]
        }
    ]
       }
        """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}