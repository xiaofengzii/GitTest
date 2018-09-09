
package cn.itcast.solr;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-solr.xml")
public class solrTest {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void testDelAll(){
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
    @Test
    public void testAdd() {
        TbItem tbItem = new TbItem();
        tbItem.setId(2L);
        tbItem.setTitle("中兴手机Axon m 折叠双屏智能手机");
        tbItem.setBrand("中兴");
        tbItem.setPrice(new BigDecimal(3777));
        tbItem.setGoodsId(123L);
        tbItem.setSeller("中兴旗舰店");
        tbItem.setCategory("手机");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();

    }
    @Test
    public void testDeleteById(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }
    @Test
    public void testDeleteByQuery(){
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
    @Test
    public void TestQueryInPage(){
        SimpleQuery query = new SimpleQuery("*:*");
       // query.setOffset(20);分页起始索引号默认为0
      //  query.setRows(20);分页页大小,默认为10
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        showPage(scoredPage);
    }

    private void showPage(ScoredPage<TbItem> scoredPage) {
        System.out.println("总记录数为:"+scoredPage.getTotalElements());
        System.out.println("总页数为:"+scoredPage.getTotalPages());
        List<TbItem> content = scoredPage.getContent();
        for (TbItem item : content) {
            System.out.println(item);
        }
    }

    @Test
    public void testMultiQuery(){
        SimpleQuery query = new SimpleQuery();
        Criteria criteria = new Criteria("item_title").contains("中兴");
        query.addCriteria(criteria);

        Criteria criteria2 = new Criteria("item_price").greaterThanEqual(1000);
        query.addCriteria(criteria2);
         // query.setOffset(20); //分页起始索引号默认为0
        //  query.setRows(20); //分页页大小,默认为10
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        showPage(scoredPage);

    }


}
