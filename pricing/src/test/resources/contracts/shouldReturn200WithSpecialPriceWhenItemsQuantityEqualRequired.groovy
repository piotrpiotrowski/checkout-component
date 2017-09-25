import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'POST'
        url "/items/discounts"
        body("""
        [{
                      "id"      : 1,
                      "quantity": 3,
                      "price": null
              },
              {
                      "id"      : 2,
                      "quantity": 2,
                      "price": null
              },
              {
                      "id"      : 3,
                      "quantity": 4,
                      "price": null
          }])
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
      "id": "2",
      "quantity": 2,
      "price": 15
    },
    {
      "id": "3",
      "quantity": 4,
      "price": 80
    }
  ]
}
  """)
        headers {
            header('Content-Type': 'application/json')
        }
    }
}