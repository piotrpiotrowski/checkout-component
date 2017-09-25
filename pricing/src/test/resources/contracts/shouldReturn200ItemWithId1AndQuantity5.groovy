import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/items/discounts"
        body("""
        [{
                "id"      : 1,
                "quantity": 5,
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
      "id": "1",
      "quantity": 5,
      "price": 150
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}