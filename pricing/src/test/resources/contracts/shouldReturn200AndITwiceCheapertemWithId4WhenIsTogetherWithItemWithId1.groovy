import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/items/discounts"
        body("""
        [
          {
            "id": 1,
            "quantity": 3,
            "price": null
          },
          {
            "id": 4,
            "quantity": 1,
            "price": null
          }
        ]
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
      "quantity": 3,
      "price": 90
    },
    {
      "id": "4",
      "quantity": 1,
      "price": 12.5
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}