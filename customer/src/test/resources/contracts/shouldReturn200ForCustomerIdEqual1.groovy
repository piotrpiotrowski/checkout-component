import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url "/customers/" + 1
    }
    response {
        status 200
        body("""
  {
    "id": "1",
    "name": "John"
  }
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}