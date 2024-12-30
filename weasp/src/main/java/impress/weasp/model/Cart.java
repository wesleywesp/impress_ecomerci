package impress.weasp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Evita serializar o usuário ao serializar o carrinho
    private User user;

    private int totalItems;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private BigDecimal totalAmount = BigDecimal.ZERO; // Total calculado automaticamente

    public Cart(User user, List<CartItem> items) {
        this.user = user;
        this.items = items;
        this.totalItems = items.size();
        updateTotalAmount();
    }

    public void updateTotalAmount() {
        this.totalAmount = this.items.stream()
                .map(item -> item.getPrice()) // Usa o preço já calculado do item
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Soma todos os preços
    }


}

