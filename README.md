#### Start Spring Cloud
```bash
spring cloud eureka
``` 

#### Test Application
```bash
http :8080/
```


#### Openshift
```bash
oc new-app  https://github.com/marzelwidmer/camel-demo.git --strategy=pipeline
```

```bash
oc start-build camel-demo --follow
```

```bash
oc logs -f bc/camel-demo
```


