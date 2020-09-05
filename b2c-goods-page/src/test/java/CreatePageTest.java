import com.mr.GoodPageApplication;
import com.mr.client.GoodsClient;
import com.mr.common.utils.PageResult;
import com.mr.service.FileStaticService;
import com.mr.service.pojo.SpuBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { GoodPageApplication.class})
public class CreatePageTest {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private FileStaticService fileService;

    @Test
    public void createPage(){

        boolean flag = true;
        int page = 0;

        while (flag){
            PageResult<SpuBo> spuPage = goodsClient.querySpuByPage(page++, 30, true, "");

            if (spuPage.getItems().size() == 0){
                flag = false;
            }

            spuPage.getItems().forEach(spuBo -> {
                try {
                    fileService.createStaticHtml(spuBo.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


}
