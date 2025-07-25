package tobyspring.splearn.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@ToString
@MappedSuperclass
public class AbstractEntity {
    @Id
    @Getter(onMethod_ = {@Nullable}) // pakcage-info.java에서 @NonNullApi 선언해 두었으나, Hibernate 객체에서는 id가 null일 수 있음
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * IDE 플러그인 JPABuddy를 설치하고, @EqualsAndHashCode 선언하면 경고가 뜸
     * JPABuddy 플러그인이 제안한 방식으로 적용
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AbstractEntity that = (AbstractEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
