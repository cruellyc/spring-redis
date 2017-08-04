package com.redis.IDao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.redis.entity.Cart;


/**
 * 购物车
 * @author liyc
 *
 */
@Repository
public interface CartDao {
	/**加载购物车列表*/
	public List<Cart> loadCartList(@Param("uid")String uid,@Param("state")int[] state,
			@Param("pNow")int pNow,@Param("pCount")int pCount);
	/**购物车记录条数*/
	public int getTotal(@Param("uid")String uid,@Param("state")int[] state);
	
	/**修改购物车状态*/
	public int updateCartState(@Param("id")String id,@Param("state")int state);
	/**加入购物车*/
	public int addCart(Cart cart);
}
