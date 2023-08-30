package feature.bid.service;

import feature.bid.dao.BidEventDao;
import feature.bid.dao.BidEventDaoImpl;
import feature.bid.dao.BidItemDao;
import feature.bid.dao.BidItemDaoImpl;
import feature.bid.vo.BidEventVo;
import feature.bid.vo.BidItemVo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BiddingServiceImpl implements BiddingService{
    private final BidItemDao dao;
    private final BidEventDao bidEventDao;
    Jedis jedis = new Jedis("localhost", 6379);
    public BiddingServiceImpl(){
        dao = new BidItemDaoImpl();
        bidEventDao = new BidEventDaoImpl();
    }
    @Override
    public List<BidItemVo> viewAll() {
        return dao.selectAll();
    }
    @Override
    public void addAnItem(BidItemVo bidItemVo) {
        dao.insert(bidItemVo);
    }

    @Override
    public BidItemVo getOneItem(Integer bidItemNo) {
        return dao.selectById(bidItemNo);
    }

    @Override
    public void removeOneItem(Integer bidItemNo) {
        System.out.println("xxx");
        dao.deleteById(bidItemNo);
    }

    @Override
    public List<BidEventVo> viewAllEvent() {
        return bidEventDao.selectAll();
    }
    @Override
    public void addAnEvent(BidEventVo bidEventVo) {
        bidEventDao.insert(bidEventVo);
    }

    @Override
    public BidEventVo getEventByNo(Integer bidEventNo) {
        return bidEventDao.selectById(bidEventNo);
    }

    @Override
    public void createOneOrder(String bidEventNo) {
        Set<Tuple> highestRecord =  jedis.zrevrangeWithScores(bidEventNo, 0, 0);
        Stream<Tuple> tupleStream = highestRecord.stream();
        tupleStream.forEach(tuple -> {
            String member = tuple.getElement();
            int score = (int)tuple.getScore();
            System.out.println("Member: " + member + ", Score: " + score);
        });
        jedis.close();


    }

}
