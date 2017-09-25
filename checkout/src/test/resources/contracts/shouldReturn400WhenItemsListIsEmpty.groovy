import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/customers/1/orders"
        body("""
          {
          "items": [
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
        "message": "Order has to have at least one item",
        "code": "empty-items-list",
        }
    ]
       }
        """)
        headers {
            header('Content-Type': 'application/json;charset=UTF-8')
        }
    }
}