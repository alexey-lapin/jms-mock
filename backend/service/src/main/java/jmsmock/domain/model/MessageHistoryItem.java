package jmsmock.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.time.ZonedDateTime;
import java.util.SortedSet;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class MessageHistoryItem implements Comparable <MessageHistoryItem> {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;
    private String payload;
    private ZonedDateTime createdAt;
    private String referenceKey;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("key ASC")
    @JoinColumn(name = "message_history_item_id")
    private SortedSet<MessageHistoryItemHeader> headers;

    @Override
    public int compareTo(MessageHistoryItem o) {
        return this.createdAt.compareTo(o.createdAt);
    }

}
