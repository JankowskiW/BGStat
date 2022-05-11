package pl.wj.bgstat.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.shop.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
