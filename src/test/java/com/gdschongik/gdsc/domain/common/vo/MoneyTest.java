package com.gdschongik.gdsc.domain.common.vo;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Nested
    class 금액_동등성_확인할때 {

        @Test
        void 값과_스케일_모두_같으면_동일한_금액이다() {
            // given
            Money money1 = Money.from(BigDecimal.valueOf(1000));
            Money money2 = Money.from(BigDecimal.valueOf(1000));

            // when & then
            assertThat(money1).isEqualTo(money2);
        }

        @Test
        void 스케일이_달라도_같은_값이면_동일한_금액이다() {
            // given
            Money money1 = Money.from(BigDecimal.valueOf(1000));
            Money money2 = Money.from(BigDecimal.valueOf(1000.0));
            Money money3 = Money.from(BigDecimal.valueOf(1000.00));
            Money money4 = Money.from(BigDecimal.valueOf(1000.000));
            Money money5 = Money.from(BigDecimal.valueOf(1000.0000));

            // when & then
            assertThat(money1)
                    .isEqualTo(money2)
                    .isEqualTo(money3)
                    .isEqualTo(money4)
                    .isEqualTo(money5);
        }

        @Test
        void 다른_값이면_다른_금액이다() {
            // given
            Money money1 = Money.from(BigDecimal.valueOf(1000.01));
            Money money2 = Money.from(BigDecimal.valueOf(1000.02));

            // when & then
            assertThat(money1).isNotEqualTo(money2);
        }
    }

    // hashCode
    @Nested
    class 금액_해시코드_확인할때 {

        @Test
        void 값과_스케일_모두_같으면_동일한_해시코드이다() {
            // given
            Money money1 = Money.from(BigDecimal.valueOf(1000));
            Money money2 = Money.from(BigDecimal.valueOf(1000));

            // when & then
            int expected = money2.hashCode();
            assertThat(money1.hashCode()).isEqualTo(expected);
        }

        @Test
        void 스케일이_달라도_같은_값이면_동일한_해시코드이다() {
            // given
            Money money1 = Money.from(BigDecimal.valueOf(1000));
            Money money2 = Money.from(BigDecimal.valueOf(1000.0));
            Money money3 = Money.from(BigDecimal.valueOf(1000.00));
            Money money4 = Money.from(BigDecimal.valueOf(1000.000));
            Money money5 = Money.from(BigDecimal.valueOf(1000.0000));

            // when & then
            assertThat(money1.hashCode())
                    .isEqualTo(money2.hashCode())
                    .isEqualTo(money3.hashCode())
                    .isEqualTo(money4.hashCode())
                    .isEqualTo(money5.hashCode());
        }

        @Test
        void 다른_값이면_다른_해시코드이다() {
            // given
            Money money1 = Money.from(BigDecimal.valueOf(1000.01));
            Money money2 = Money.from(BigDecimal.valueOf(1000.02));

            // when & then
            assertThat(money1.hashCode()).isNotEqualTo(money2.hashCode());
        }
    }
}
