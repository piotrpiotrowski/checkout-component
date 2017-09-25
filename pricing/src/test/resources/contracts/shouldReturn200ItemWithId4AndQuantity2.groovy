import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/items/discounts"
        body("""
        [{
                "id"      : 4,
                "quantity": 2,
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
      "id": "4",
      "quantity": 2,
      "price": 47
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}