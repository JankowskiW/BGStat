package pl.wj.bgstat.store;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.store.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
