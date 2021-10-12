package features;

import org.noear.solon.Solon;
import org.noear.solon.SolonBuilder;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.RenderManager;
import org.noear.solon.serialization.JsonLongConverter;
import org.noear.solon.serialization.JsonStringConverter;
import org.noear.solon.serialization.fastjson.FastjsonRenderFactory;
import org.noear.solon.serialization.snack3.SnackRenderFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear 2021/10/12 created
 */
@Controller
public class TestApp {
    public static void main(String[] args) {
        new SolonBuilder()
                .onPluginLoadEnd((e) -> {
                    //换成特定的渲染器
                    RenderManager.mapping("@json", FastjsonRenderFactory.global.create());
                })
                .start(TestApp.class, args, app -> {
                    initMvcJsonCustom();
                });
    }

    /**
     * 初始化json定制（需要在插件运行前定制）
     * */
    private static void initMvcJsonCustom(){
        //通过转换器，做简单类型的定制
        FastjsonRenderFactory.global.addConvertor(Date.class,
                (JsonLongConverter<Date>) source -> source.getTime());

        FastjsonRenderFactory.global.addConvertor(LocalDate.class,
                (JsonStringConverter<LocalDate>) source -> source.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        FastjsonRenderFactory.global.addConvertor(LocalDateTime.class,
                (JsonStringConverter<LocalDateTime>) source -> source.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

    }

    @Mapping("/")
    public Object home(){
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("time1", LocalDateTime.now());
        data.put("time2", LocalDate.now());
        data.put("time3", new Date());

        return data;
    }
}