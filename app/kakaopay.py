from flask import Flask, request, jsonify, redirect
import requests

app = Flask(__name__)

KAKAO_ADMIN_KEY = "YOUR_KAKAO_ADMIN_KEY"  # 여기에 본인의 관리자 키를 입력

@app.route('/pay/kakao', methods=['POST'])
def kakao_pay():
    headers = {
        "Authorization": f"KakaoAK {KAKAO_ADMIN_KEY}",
        "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
    }

    params = {
        "cid": "TC0ONETIME",  # 테스트용 CID
        "partner_order_id": "1001",
        "partner_user_id": "user123",
        "item_name": "ICE 아메리카노",
        "quantity": 1,
        "total_amount": 1500,
        "vat_amount": 200,
        "tax_free_amount": 0,
        "approval_url": "http://localhost:5000/pay/success",
        "cancel_url": "http://localhost:5000/pay/cancel",
        "fail_url": "http://localhost:5000/pay/fail"
    }

    response = requests.post("https://kapi.kakao.com/v1/payment/ready", headers=headers, data=params)
    res_json = response.json()

    print("카카오페이 응답:", res_json)

    return jsonify({
        "redirect_url": res_json["next_redirect_pc_url"]
    })

if __name__ == '__main__':
    app.run(debug=False)