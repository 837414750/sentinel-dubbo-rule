- 限流（热点参数限流）
- /queryHot接口, 控制器使用对象接收，请求参数是：UserRequest

- 附件文件位置：dubbo-consumer->resource->img下的图片


- 热点参数核心代码
```

@RequestMapping("/queryHot")
public String queryHot(@RequestBody UserRequest userRequest) {
   return testHotParamService.queryHot(userRequest);
}
    

@Service
public class TestHotParamService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @SentinelResource("queryHot-sentinel")
    public String queryHot(UserRequest userRequest) {

        return (String) execute(userRequest);
    }

    public Object execute(UserRequest userRequest) {
        Entry entry = null;
        try {
            entry = SphU.entry("queryHot-sentinel", EntryType.IN, 1, userRequest.getName());

            // Your logic here.
            return "Hello " + userRequest.getName();
        } catch (BlockException ex) {
            // Handle request rejection.
            log.error("异常：", ex);
            return "~~~~~~限流了·~~~~~~";
        } finally {
            if (entry != null) {
                entry.exit(1, userRequest.getCode());
            }
        }
    }

}
```
