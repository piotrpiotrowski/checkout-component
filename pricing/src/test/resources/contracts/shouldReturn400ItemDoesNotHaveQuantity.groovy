import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/items/discounts"
        body([
                "id"      : $(regex('[0-9]*')),
                "quantity": null
        ])
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
    "status": 400,
    "details":[
        {
         "code": "not-positive-number",
         "field":"item[0].quantity",
         "value": null
        }
    ]
  }
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}