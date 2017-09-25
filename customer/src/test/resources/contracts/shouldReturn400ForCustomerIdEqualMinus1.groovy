import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url "/customers/" + "-1"
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
         "code": "negative-value",
         "field":"customerId",
         "value":-1
        }
    ]
  }
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}