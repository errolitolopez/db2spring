package ${packageRepository};

${classImports}
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ${classNameRepository} extends JpaRepository<${classNameEntity}, ${idColumn.javaType}>, JpaSpecificationExecutor<${classNameEntity}> {
}
