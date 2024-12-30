package impress.weasp.service;

import impress.weasp.model.Cart;
import impress.weasp.model.CartItem;
import impress.weasp.model.Product;
import impress.weasp.model.User;

public interface CartService {
    public CartItem addProductToCart(User user, Product product,Integer quantity);

    public Cart findUserCard(User user);
}
