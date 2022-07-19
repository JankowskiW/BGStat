package pl.wj.bgstat.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wj.bgstat.domain.store.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
