package org.devkirby.hanimman.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.devkirby.hanimman.dto.ShareTogetherDTO;

import jakarta.persistence.Query;

@RequiredArgsConstructor
@Service
public class ShareTogetherServiceImpl implements ShareTogetherService {
/*

SELECT c.id, 
       c.content, 
       c.created_at, 
       c.modified_at, 
       c.address_id, 
       c.date_at, 
       c.quantity, 
       c.price, 
       c.user_id, 
       c.`type`, 
       u.`name`, 
       a.`city_name`
FROM (
    SELECT id, 
           content, 
           created_at, 
           modified_at, 
           address_id, 
           location_date AS date_at, 
           quantity, 
           price, 
           user_id, 
           'shares' AS `type`
    FROM shares
	 WHERE deleted_at IS NULL
	 AND is_end = 0
    
    UNION ALL
    
    SELECT id, 
           content, 
           created_at, 
           modified_at, 
           address_id, 
           meeting_at AS date_at, 
           quantity, 
           price, 
           user_id, 
           'togethers' AS `type`
    FROM togethers
	 WHERE deleted_at IS NULL
	 AND is_end = 0
) AS c
LEFT JOIN addresses a ON a.id = c.address_id
LEFT JOIN users u ON u.id = c.user_id
ORDER BY c.created_at DESC
LIMIT 5;

 */
    private final String NATIVE_QUERY = """
SELECT 
    s.id, 
    s.content, 
    s.created_at, 
    s.modified_at, 
    s.address_id, 
    s.location_date AS date_at, 
    s.quantity, 
    s.price, 
    s.user_id, 
    'shares' AS `type`,
    u.`name`,
    si.id AS image_id
FROM shares s
JOIN users u ON u.id = s.user_id
LEFT JOIN share_images si ON si.parent_id = s.id AND si.deleted_at IS NULL
WHERE s.deleted_at IS NULL
  AND s.is_end = 0

UNION ALL

SELECT 
    t.id, 
    t.content, 
    t.created_at, 
    t.modified_at, 
    t.address_id, 
    t.meeting_at AS date_at, 
    t.quantity, 
    t.price, 
    t.user_id, 
    'togethers' AS `type`,
    u.`name`,
    ti.id AS image_id
FROM togethers t
LEFT JOIN users u ON u.id = t.user_id
LEFT JOIN together_images ti ON ti.parent_id = t.id AND ti.deleted_at IS NULL
WHERE t.deleted_at IS NULL
  AND t.is_end = 0

ORDER BY created_at DESC
LIMIT 5;
            """;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<ShareTogetherDTO> getSharesAndTogethers() {
        Query query = entityManager.createNativeQuery(NATIVE_QUERY);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        List<ShareTogetherDTO> dtos = results.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return dtos;
    }

    private ShareTogetherDTO convertToDto(Object[] result) {
        return new ShareTogetherDTO(
                (Integer) result[0],                // id
                (String) result[1],                 // content
                convertTimestamp(result[2]),        // createdAt
                convertTimestamp(result[3]),        // modifiedAt
                (String) result[4],                // addressId
                convertTimestamp(result[5]),        // dateAt
                (Integer) result[6],                // quantity
                (Integer) result[7],                 // price
                (Integer) result[8],                // userId
                (String) result[9],                 // type
                (Integer) result[10]                 // imageId
        );
    }

    private LocalDateTime convertTimestamp(Object timestampObj) {
        return timestampObj != null ? ((Timestamp) timestampObj).toLocalDateTime() : null;
    }
}
