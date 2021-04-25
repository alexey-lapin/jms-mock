package jmsmock.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Parameter {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;

    private String key;

    private String value;

}
