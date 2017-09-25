import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/items/discounts"
        body("""
        [{
                "id"      : 2,
                "quantity": 1,
                "price": null
        }]
        """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
    response {
        status 200
        body("""
  {
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
            header('Content-Type': 'application/json')
        }
    }
}