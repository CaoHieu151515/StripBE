import React, { useState, useEffect, useRef } from 'react';
import DropIn from 'braintree-web-drop-in-react';
import axios from 'axios';
import qs from 'qs';

// ✅ Lấy token từ localStorage JHipster
const getToken = () => localStorage.getItem('jhi-authenticationToken');

const BraintreeDropIn = () => {
  const [clientToken, setClientToken] = useState<string | null>(null);
  const [amount, setAmount] = useState('');
  const dropinInstance = useRef<any>(null);
  const token = getToken();

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/payment/braintree/client-token`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then(response => {
        setClientToken(response.data);
      })
      .catch(error => {
        console.error('❌ Lỗi khi lấy client token:', error);
        alert('Không thể khởi tạo thanh toán. Vui lòng thử lại.');
      });
  }, [token]);

  const handlePayment = async () => {
    if (!dropinInstance.current) {
      alert('Drop-in chưa sẵn sàng.');
      return;
    }

    try {
      const { nonce } = await dropinInstance.current.requestPaymentMethod();

      const requestData = {
        nonce,
        amount: parseFloat(amount),
      };

      const response = await axios.post(`http://localhost:8080/api/payment/braintree/checkout`, qs.stringify(requestData), {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        alert('✅ Nạp tiền thành công!');
        setAmount('');
      } else {
        alert('❌ Nạp tiền thất bại!');
      }
    } catch (error) {
      console.error('❌ Lỗi xử lý thanh toán:', error);
      alert('Giao dịch thất bại. Vui lòng thử lại.');
    }
  };

  return (
    <div style={{ padding: '2rem', maxWidth: '500px', margin: 'auto' }}>
      <h2>💳 Nạp Tiền qua Braintree</h2>

      <input
        type="number"
        value={amount}
        onChange={e => setAmount(e.target.value)}
        placeholder="Nhập số tiền"
        style={{ width: '100%', padding: '10px', marginBottom: '20px' }}
      />

      {clientToken ? (
        <DropIn
          options={{
            authorization: clientToken,
            card: {
              cardholderName: {
                required: true,
              },
              overrides: {
                fields: {
                  cvv: {
                    required: true,
                  },
                },
              },
            },
          }}
          onInstance={instance => (dropinInstance.current = instance)}
        />
      ) : (
        <p>🔄 Đang khởi tạo thanh toán...</p>
      )}

      <button
        onClick={handlePayment}
        style={{
          marginTop: '20px',
          padding: '10px 20px',
          fontSize: '16px',
          cursor: 'pointer',
        }}
      >
        Nạp Tiền
      </button>
    </div>
  );
};

export default BraintreeDropIn;
