from flask import Flask, request, jsonify, redirect
import requests

app = Flask(__name__)

KAKAO_ADMIN_KEY = 'YOUR_ADMIN_KEY'  # 여기 관리자 키 입력

@app.route('/pay/kakao', methods=['POST'])
def kakao_pay():
    data = request.json
    headers = {
        "Authorization": f"KakaoAK {KAKAO_ADMIN_KEY}",
        "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
    }

    params = {
        "cid": "TC0ONETIME",
        "partner_order_id": "order123",
        "partner_user_id": "user123",
        "item_name": data['item_name'],
        "quantity": data['quantity'],
        "total_amount": data['total_amount'],
        "vat_amount": 0,
        "tax_free_amount": 0,
        "approval_url": "http://localhost:5000/pay/success",
        "cancel_url": "http://localhost:5000/pay/cancel",
        "fail_url": "http://localhost:5000/pay/fail"
    }

    response = requests.post("https://kapi.kakao.com/v1/payment/ready", headers=headers, data=params)
    result = response.json()
    return jsonify({"redirect_url": result['next_redirect_pc_url']})

@app.route('/pay/success')
def success():
    return "<h1>결제 성공</h1>"

@app.route('/pay/cancel')
def cancel():
    return "<h1>결제 취소</h1>"

@app.route('/pay/fail')
def fail():
    return "<h1>결제 실패</h1>"

if __name__ == '__main__':
    app.run(debug=True)