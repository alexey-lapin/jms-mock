package jmsmock.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class MessageHistoryItemHeader implements Comparable<MessageHistoryItemHeader> {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;
    private String key;
    private String value;

    @Override
    public int compareTo(MessageHistoryItemHeader o) {
        return this.key.compareTo(o.key);
    }

}
